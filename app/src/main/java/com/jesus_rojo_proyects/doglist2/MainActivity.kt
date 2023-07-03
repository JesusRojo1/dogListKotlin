package com.jesus_rojo_proyects.doglist2

import adapters.DogAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jesus_rojo_proyects.doglist2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import providers.ApiService
import responses.DogsResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {
    //Llamamos al activitymainbinding, esta clase llamara a la vista por ende
    //ya no tendremos que hacer uso del setcontentview()
    private lateinit var binding: ActivityMainBinding

    //necesitamos pasarle el adapter y para que el mainactivity lo agarra
    //hacemos lo siguiente:
    private lateinit var adapter: DogAdapter

    //ahora para obtener las images (ya que el adapter no las tiene)
    //creamos una constante que sea tipo mutable list string, debido
    //a que las imagenes del servicios es una lista de strings recuerda
    private val dogImages = mutableListOf<String>()
    //entonces vamos al initRecyclerView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //fijate que se procede a comentar el setcontentview()
        //por ende ya no tenemos como llamar a la vista.
        //setContentView(R.layout.activity_main)
        //Ahora para llamar la vista usaremos el binding de arriba.
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Ahora si usaremos el setContentView(), pero como parametro
        //no usaremos el R.layout. activi....
        //si no, que usaremos el binding
        setContentView(binding.root)
        //ahora para acceder a todos los elementos de las vistas
        //usaremos el binding.idDelObjeto
        binding.searchDog.setOnQueryTextListener(this)
        initRecyclerView()
    }

    // para las peticiones se recomienda crear funciones que sean de tipo retrofit
    private fun getRetrofit(): Retrofit {
        //a continuacion usamos el metodo retrofit.builder, para hacer uso de la base url
        //y asi pasar nuestro link. a continuacion addconverterfactory que seria para
        //crear el gsonconverterfactory. y por ultimo el build, este puede ir en la zona baja
        //de la funcion o al lado del tipo de la funcion "fun getRetrofit(): Retrofit.Builder".
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //NOTA: COMO ESTAMOS EN UNA CORRUTINA, LAS FUNCIONES
    //QUE SON ASINCRONAS, EJEMPLO: "getDogsByBreeds"
    //DEBEN DE TENER UN SUSPEND ANTES DEL FUN

    //CORRUTINAS == HILOS == procesos async
    //se crea una funcion nueva, determinando que el parametro query sera de tipo string
    //recuerda que el SCOPE es el uso de los valores internos de un bloque de codigo y como se puede
    //propagar / usar en el resto del sistema
    private fun searchByName(query: String) {
        //HILO SEGUNDARIO
        //var callService: Response<DogsResponse>
        CoroutineScope(Dispatchers.IO).launch {
            //ya creamos el hilo ahora a programar que hara dicho hilo
            //creamos una variable const que en kotlin es "val", la definimos y le decimos,
            //que sera tipo response y le indicamos que sera del <modelo>
            //procedemos a igualar a la funcion donde tenemos la url del api,
            //le indicamos que haga el llamado al servicio api "ApiService",
            //y entramos a la funcion que creamos en la interface / servicio para,
            //pasarle la url que resta que nos ayudara con la api
            val callService: Response<DogsResponse> =
                getRetrofit().create(ApiService::class.java).getDogsByBreeds("$query/images")
            val dogs: DogsResponse? = callService.body()
            //Si te percatas, veras que usamos el argumento query con el fin de completar la url
            //que en este caso seria la raza del perro.

            //Ahora para comprobar que nos respondio correctamente la api hacemos lo siguiente
            // HILO PRINCIPAL RETORNO
            runOnUiThread {
                if (callService.isSuccessful) {
                    //mostrar en el recycleview
                    //almacenamos las imagenes primero
                    //NOTA: SI QUIERES VERIFICAR SI UNA VARIABLE
                    //ES NULA O NO, EVITAR QUE MUESTRE NULLO O VACIO
                    //EN LA VISTA USA EL PELO DE ELVIS "?:"
                    //EJEMPLO:
                    //SI "dogs.imagesDog" NO ES NULO, MOSTRARA LAS
                    //IMAGENES, PERO SI ES NULO MOSTRARA UNA LISTA
                    //VACIA. EL ?: ES EL "SI NO, ENTONCES HAZ ESTO"
                    print(dogs?.status)
                    val images: List<String> = dogs?.imagesDog ?: emptyList()
                    //Ahora tomamos al dogImages (que esta en el mainactivity)
                    //y le borramos toda la data
                    dogImages.clear()
                    //y ahora le agregamos todaaaa la data
                    dogImages.addAll(images)
                    //por ultimo le avisamos al adapter que creamos,
                    //que han habido cambios de esta manera
                    adapter.notifyDataSetChanged()
                    //ahora solo falta programar la funcion del buscado,
                    //porque sin el solo esta la vista pero no funciona.
                    //para eso llamamos al onQueryTextListener en el tipo
                    //de mainactivity
                } else {
                    //mostrar error
                    print(dogs?.status)
                    showError()
                }
            }
        }
        //FIN DEL HILO SEGUNDARIO
    }

    //metodo si responde error la peticion a la api:
    private fun showError() {
        Toast.makeText(this, "The service isn't available", Toast.LENGTH_SHORT).show()
    }

    //    val callService: Response<DogsResponse> =
//        getRetrofit().create(ApiService::class.java).getDogsByBreeds("$query/images")
//    if (callService.isSuccessful) {
//        //show recycleview
//    } else {
//        //show error
//    }
    //AHORA CREAREMOS UN ADAPTER DENTRO DEL ONCREATE
    private fun initRecyclerView() {
        //llamamos al adapter y le pasamos la imagen
        //los cuales declaramos en el MainActivity
        adapter = DogAdapter(dogImages)
        // aqui hacemos llamado al layout (al recycleview para
        // que sepa que esta esperando una data)
        binding.listViewDogs.layoutManager = LinearLayoutManager(this)
        //ahora pintamos dicha data en el layout de recycleview
        //de esta manera:
        binding.listViewDogs.adapter = adapter

    }

    //ESTOS SON LOS METODOS DEL BUSCADOR PARA SU FUNCIONALIDAD

    //este es el que nos interesa, este cuando termine de escribir
    //lo que esta buscando el usuario y le das a buscar (enter)
    //llamara a este metodo
    override fun onQueryTextSubmit(query: String?): Boolean {
        //si el query ingresado por el usuario,
        //no es vacio ni nulo entonces:
        if (!query.isNullOrEmpty()) {
            searchByName(query.lowercase())
        }
        //al igual que el metodo de abajo "onQueryTextChange"
        //debemos devolver un booleano
        return true
    }

    //nos avisara cuando el usuario haga un cambio en el campo
    //de busqueda, este devuelve un booliano
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}