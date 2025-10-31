package ec.edu.uisek.calculator

//se importa desde mutablestateof ya que vamos a obtener y dar algo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class CalculatorState(
    val display: String = "0"
)

//nos sirve para mapear los eventos
sealed class CalculatorEvent {
    //vamos a manejar cuando se presione en numeros o operaciones
    data class Number(val number: String) : CalculatorEvent()
    data class Operator(val operator: String) : CalculatorEvent()

    //lo cramos asi porque no va a recibir ningun parametro
    object Clear : CalculatorEvent()
    object AllClear : CalculatorEvent()
    object Calculate : CalculatorEvent()
    object Decimal : CalculatorEvent()

}

///heredamos ViewModel
class CalculatorViewModel : ViewModel() {

    //vamos a almacenoar estos dos factores en cadenas
    private var number1: String = ""
    private var number2: String = ""
    private var operator: String? = null //significa que puede ser nula  o string

    //vamos a controlar el calculatorstate
    //asi pueden ver el cambio que vamos a calcular
    var state by mutableStateOf(CalculatorState()) // escucha los cambios del cambio de estado
        //vamos a setear el valor de state
        private set

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterNumber(event.number)
            is CalculatorEvent.Operator -> enterOperator(event.operator)
            is CalculatorEvent.Decimal -> enterDecimal()
            is CalculatorEvent.Clear -> clearLast()
            is CalculatorEvent.AllClear -> clearAll()
            is CalculatorEvent.Calculate -> performCalculation()
        }
        //va a mapear a todos los objetos que tenemos en calcularevent
    }

    ///AQUI comenzamos a implementar las clases


    private fun clearAll() {
        //reseteamos los valores al original
        number1 = ""
        number2 = ""
        operator = null
        state = state.copy("0")
    }

    private fun clearLast() {
        //vamo a borrar el ultimo elemento que ingresa
        //verifico que si hay o no operador
        if (operator == null) {
            //verifico si existe un numero
            if (number1.isNotBlank()) {
                //si no esta en blanco lo elimina
                number1 = number1.dropLast(1)
                //actualizo el estado para que se muestre en pantalla
                state = state.copy(if (number1.isBlank()) "0" else number1)
            }
        } else {
            //lo mimso para el numero2
            if (number2.isNotBlank()) {
                number2 = number2.dropLast(1)
                state = state.copy(if (number2.isBlank()) "0" else number2)
            } else {
                operator = null
                state = state.copy(number1)
            }
        }
    }

    private fun enterDecimal() {
        val currentNumber = if (operator == null) number1 else number2
        if (!currentNumber.contains(".")) {
            if (operator == null) {
                number1 += "."
                state = state.copy(display = number1)
            } else {
                number2 += "."
                state = state.copy(display = number2)
            }
        }
    }

    private fun enterOperator(operator: String) {
        //le indicamos que estamos usando el paramentro de esta clase
        if (number1.isNotBlank() && this.operator != null && number2.isNotBlank()) {
            performCalculation()
        }
        this.operator = operator
        state = state.copy(display = operator)
    }

    private fun enterNumber(number: String) {
        //va concatenando cada numero en la pnatalla
        if (operator == null) {
            number1 += number
            state = state.copy(display = number1)
        } else {
            number2 += number
            state = state.copy(display = number2)
        }
    }


    private fun performCalculation() {
        //vamos a calcular
        //tenemos que convertir datos string a enteros o decimales
        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()

        //verifico si tengo o no numeros
        if (num1 != null && num2 != null && operator != null) {
            val result = when (operator) {
                "×" -> num1 * num2
                "−" -> num1 - num2
                "+" -> num1 + num2
                // indicamos que no sea cero y si es cero entonces me da no es un numero (NaN)
                "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0

            }
            clearAll()
            val resultString = if (result.isNaN()) "Error" else result.toString().removeSuffix(".0")
            number1 = if (result.isNaN()) "" else resultString
            state = state.copy(resultString)
        }
    }
}