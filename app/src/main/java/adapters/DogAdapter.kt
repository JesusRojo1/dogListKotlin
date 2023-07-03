package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jesus_rojo_proyects.doglist2.R

class DogAdapter(val images:List<String>): RecyclerView.Adapter<DogViewHolder>() {
    //y este es el metodo mas complicado entre los 3
    //debido a toda la linea de codigos que hay
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DogViewHolder(layoutInflater.inflate(R.layout.item_dog, parent, false))
    }
    //este metodo nos permite pasar el item
    //de la posicion donde se encuentra dentro de
    //la lista de string que nos devuelve el api
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val item:String = images[position]
        holder.bind(item)
    }
    //este es el metodo mas facil
    //retornara el tama;o de las imagenes
    //puedes trabajarlo en una linea o
    //en un bloque de codigo
    //ejemplo:  override fun getItemCount(): Int = images.size
    override fun getItemCount(): Int {
        return images.size
    }
}