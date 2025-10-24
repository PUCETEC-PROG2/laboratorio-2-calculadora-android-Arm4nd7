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
    data class Number(val number: String): CalculatorEvent()
    data class Operator(val operator: String): CalculatorEvent()

    //lo cramos asi porque no va a recibir ningun parametro
    object Clear : CalculatorEvent()
    object AllClear : CalculatorEvent()
    object Calculate : CalculatorEvent()
    object Decimal : CalculatorEvent()

}

///heredamos ViewModel
class CalculatorViewModel: ViewModel(){

    //vamos a almacenoar estos dos factores en cadenas
    private var number1 : String = ""
    private var number2 : String = ""
    private var operator : String? = null //significa que puede ser nula  o string

    //vamos a controlar el calculatorstate
    //asi pueden ver el cambio que vamos a calcular
    var state by mutableStateOf(CalculatorState()) // escucha los cambios del cambio de estado
        //vamos a setear el valor de state
        private set

    fun onEvent(event: CalculatorEvent){
        when(event){
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
    private fun performCalculation() {
        TODO("Not yet implemented")
    }

    private fun clearAll() {
        TODO("Not yet implemented")
    }

    private fun clearLast() {
        TODO("Not yet implemented")
    }

    private fun enterDecimal() {
        val currentNumber = if(operator == null) number1 else number2
        if (!currentNumber.contains(".")){
            if (operator == null){
                number1 += "."
                state = state.copy(display = number1)
            }else {
                number2 += "."
                state = state.copy(display = number2)
            }
        }
    }

    private fun enterOperator(operator: String) {
        if (number1.isNotBlank()){
            //le indicamos que estamos usando el paramentro de esta clase
            this.operator = operator
        }
    }

    private fun enterNumber(number: String) {
        //va concatenando cada numero en la pnatalla
        if(operator == null){
            number1 += number
            state = state.copy(display = number)
        }else{
            number2 += number
            state = state.copy(display = number2)
        }
    }
}