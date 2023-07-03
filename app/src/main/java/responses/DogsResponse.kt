package responses

import com.google.gson.annotations.SerializedName

//Para tener un get y consultar a la api necesitamos un modelo
//para esto necesitamos ver que nos devuelve la api al consultar
//en este caso nos regresa "message" que es una lista de string y
//"status" que es un string.

//Ahora si preferimos que los parametros NO SE LLAMEN como en el json
//que envia la api, pues podremos cambiarle el nombre de la variable
//SIN PERDER EL NOMBRE DEL MODELO ejemplo modelo => message / variable => images
//modelo pertenece a la peticion y variable a como manejaremos la data en la
//aplicacion es decir, donde se almacena toda la data de la api que esta en el modelo.
//Esto se logra con "@SerializedName("Nombre del modelo") Ejemplo:
//"@SerializedName("statos / message")
data class DogsResponse(
    @SerializedName("status") var status: String,
    @SerializedName("message") var imagesDog: List<String>
)