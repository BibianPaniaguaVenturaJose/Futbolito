package dev.ricknout.composesensors.demo.ui.accelerometer
import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import dev.ricknout.composesensors.demo.model.Demo
import dev.ricknout.composesensors.demo.ui.Demo
import dev.ricknout.composesensors.demo.ui.NotAvailableDemo

@Composable
fun AccelerometerDemo() {
    if (isAccelerometerSensorAvailable()) {
        val sensorValue by rememberAccelerometerSensorValueAsState()
        var (x, y, z) = sensorValue.value

        val Velocidad = 4.0f

        var topGoalCount by remember { mutableStateOf(0) }
        var bottomGoalCount by remember { mutableStateOf(0) }

        Demo(
            demo = Demo.ACCELEROMETER,
            // value = "X: $x m/s^2\nY: $y m/s^2\nZ: $z m/s^2",
        ) {
            //variables del acelerometro
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()
            var center by remember { mutableStateOf(Offset(width / 2, height / 2)) }
            val orientation = LocalConfiguration.current.orientation
            val contentColor = LocalContentColor.current
            val radius = with(LocalDensity.current) { 10.dp.toPx() }

            //ancho de la porteria y tamaño acorde a la pantalla
            val goalHeight = with(LocalDensity.current) { 20.dp.toPx() }
            val goalWidth = width / 5

            //posicion de las porterias
            val topGoalRect = Offset((width - goalWidth) / 2, 0f) to Offset((width + goalWidth) / 2, goalHeight)
            val bottomGoalRect = Offset((width - goalWidth) / 2, height - goalHeight) to Offset((width + goalWidth) / 2, height)

            // Define las coordenadas y dimensiones de los bloques de obstáculos adicionales
            val obstacleRect1 = Offset(width / 8, height / 5) to Offset(width / 8 + 50f, height / 5 + 150f)
            val obstacleRect2 = Offset(width / 8, height * 2 / 3) to Offset(width / 8 + 50f, height * 2 / 3 + 150f)
            val obstacleRect3 = Offset(7 * width / 8 - 50f, height / 5) to Offset(7 * width / 8, height / 5 + 150f)
            val obstacleRect4 = Offset(7 * width / 8 - 50f, height * 2 / 3) to Offset(7 * width / 8, height * 2 / 3 + 150f)

            val obstacleRect5 = Offset(width / 3, height / 3) to Offset(width / 3 + 50f, height / 3 + 200f)
            val obstacleRect6 = Offset(width / 3, height / 3 + 250f) to Offset(width / 3 + 50f, height / 3 + 450f)
            val obstacleRect7 = Offset(width / 1.5f, height / 3) to Offset(width / 1.5f + 50f, height / 3 + 200f)
            val obstacleRect8 = Offset(width / 1.5f, height / 3 + 250f) to Offset(width / 1.5f + 50f, height / 3 + 450f)


            // Ajusta la lógica de colisión para tener en cuenta estos bloques
            val obstacleCollision1 = center.x in obstacleRect1.first.x..obstacleRect1.second.x && center.y in obstacleRect1.first.y..obstacleRect1.second.y
            val obstacleCollision2 = center.x in obstacleRect2.first.x..obstacleRect2.second.x && center.y in obstacleRect2.first.y..obstacleRect2.second.y
            val obstacleCollision3 = center.x in obstacleRect3.first.x..obstacleRect3.second.x && center.y in obstacleRect3.first.y..obstacleRect3.second.y
            val obstacleCollision4 = center.x in obstacleRect4.first.x..obstacleRect4.second.x && center.y in obstacleRect4.first.y..obstacleRect4.second.y
            val obstacleCollision5 = center.x in obstacleRect5.first.x..obstacleRect5.second.x && center.y in obstacleRect5.first.y..obstacleRect5.second.y
            val obstacleCollision6 = center.x in obstacleRect6.first.x..obstacleRect6.second.x && center.y in obstacleRect6.first.y..obstacleRect6.second.y
            val obstacleCollision7 = center.x in obstacleRect7.first.x..obstacleRect7.second.x && center.y in obstacleRect7.first.y..obstacleRect7.second.y
            val obstacleCollision8 = center.x in obstacleRect8.first.x..obstacleRect8.second.x && center.y in obstacleRect8.first.y..obstacleRect8.second.y


            // Verifica si la pelota colisiona con los bloques de obstáculos
            if (obstacleCollision1 || obstacleCollision2 || obstacleCollision3 || obstacleCollision4 || obstacleCollision5 || obstacleCollision6 || obstacleCollision7 || obstacleCollision8) {
                // Cambia la dirección de la pelota
                x = -x
                y = -y
            }

            center = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Offset(
                    x = (center.x - x * Velocidad).coerceIn(radius, width - radius),
                    y = (center.y + y * Velocidad).coerceIn(radius, height - radius),
                )
            } else {
                Offset(
                    x = (center.x + y * Velocidad).coerceIn(radius, width - radius),
                    y = (center.y + x * Velocidad).coerceIn(radius, height - radius),
                )
            }

            if (center.y <= topGoalRect.second.y && center.x in topGoalRect.first.x..topGoalRect.second.x) {
                topGoalCount++
                center = Offset(width / 2, height / 2)
            } else if (center.y >= bottomGoalRect.first.y && center.x in bottomGoalRect.first.x..bottomGoalRect.second.x) {
                bottomGoalCount++
                center = Offset(width / 2, height / 2)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Dibuja porteria de arriba
                    drawRect(
                        color = Color.Red,
                        topLeft = topGoalRect.first,
                        size = androidx.compose.ui.geometry.Size(topGoalRect.second.x - topGoalRect.first.x, topGoalRect.second.y - topGoalRect.first.y)
                    )
                    // Dibuja porteria de abajo
                    drawRect(
                        color = Color.Cyan,
                        topLeft = bottomGoalRect.first,
                        size = androidx.compose.ui.geometry.Size(bottomGoalRect.second.x - bottomGoalRect.first.x, bottomGoalRect.second.y - bottomGoalRect.first.y)
                    )

                    // Dibuja los bloques de obstáculos
                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect1.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect1.second.x - obstacleRect1.first.x, obstacleRect1.second.y - obstacleRect1.first.y)
                    )

                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect2.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect2.second.x - obstacleRect2.first.x, obstacleRect2.second.y - obstacleRect2.first.y)
                    )

                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect3.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect3.second.x - obstacleRect3.first.x, obstacleRect3.second.y - obstacleRect3.first.y)
                    )
                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect4.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect4.second.x - obstacleRect4.first.x, obstacleRect4.second.y - obstacleRect4.first.y)
                    )
                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect5.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect5.second.x - obstacleRect5.first.x, obstacleRect5.second.y - obstacleRect5.first.y)
                    )
                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect6.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect6.second.x - obstacleRect6.first.x, obstacleRect6.second.y - obstacleRect6.first.y)
                    )
                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect7.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect7.second.x - obstacleRect7.first.x, obstacleRect7.second.y - obstacleRect7.first.y)
                    )
                    drawRect(
                        color = Color.Green,
                        topLeft = obstacleRect8.first,
                        size = androidx.compose.ui.geometry.Size(obstacleRect8.second.x - obstacleRect8.first.x, obstacleRect8.second.y - obstacleRect8.first.y)
                    )

                    drawCircle(
                        color = contentColor,
                        radius = radius,
                        center = center,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                        .align(Alignment.CenterStart),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$bottomGoalCount",
                        color = Color.Red,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = 90f
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "$topGoalCount",
                        color = Color.Cyan,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = 90f
                        }
                    )
                }
            }
        }
    } else {
        NotAvailableDemo(demo = Demo.ACCELEROMETER)
    }
}

fun android.graphics.RectF.toSize(): androidx.compose.ui.geometry.Size {
    return androidx.compose.ui.geometry.Size(this.right - this.left, this.bottom - this.top)
}







