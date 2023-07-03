package adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jesus_rojo_proyects.doglist2.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

//PARA QUE SON LOS HOLDERS?
//bueno los view holders son clases,
//que nos permitira mantener la informacion
//de manera individual aunque cada data
//consultada sea diferente
//es decir solo existe un solo "holder" = "titular"
//el cual mostrara la data que le toque mostrar.
class DogViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val binding = ItemDogBinding.bind(view)
    //atamos aqui la imagen de cada perro
    fun bind(image:String){
        //y cargaremos la imagen (es decir, la url)
        //que consultamos usando piccaso
        Picasso.get().load(image).into(binding.idViewCardDog)
        //aqui es donde se carga la imagen de item_dog
        binding.idViewCardDog
    }
}