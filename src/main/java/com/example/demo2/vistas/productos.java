package com.example.demo2.vistas;

import com.example.demo2.modulos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class productos extends Stage {



    private Scene escena;
    private ImageView imagenView;
    private Button boton_cargar;
    private Label txtnombre,txtprecio,txtdescripcion,txtidcategoria;
    private VBox contenedor_padre;
    private Button enviar;

    private TextField nombre;
    private TextField precio;
    private TextField descripcion;
    private TextField idcategoria;

    private ProductosDAO objeto;
    private Label title;
    private Button salir;
    private boolean option;
    private String title_;
    private String rutaIMAGEN="";

    private TableView<ProductosDAO> tabla_productos;
    private CategoriasDAO cat_categorias;
    List<Categoria> lista_de_categrorias;




    ObservableList<Categoria> observable_lista;
    ComboBox<Categoria> comboBox;

    public productos(TableView<ProductosDAO> tabla_productos,ProductosDAO objeto_dao,String title_){
        cat_categorias = new CategoriasDAO();
        lista_de_categrorias= cat_categorias.Obtener_Categorias();

        observable_lista= FXCollections.observableArrayList(lista_de_categrorias);
        comboBox=new ComboBox<>(observable_lista);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Categoria categoria_a){
                return (categoria_a!=null)?categoria_a.nombre:"";
            }

            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });

        this.title_=title_;
        this.tabla_productos=tabla_productos;
        CREAR_GUI();
        if(objeto_dao==null){
            objeto =new ProductosDAO();
            option=false;
        }
        else{
            option=true;
            objeto=objeto_dao;
            this.nombre.setText(objeto.getNombre());
            this.precio.setText(String.valueOf(objeto.getPrecio()));
            this.descripcion.setText(objeto.getDescripcion());
        }
        //objeto = (objeto_dao==null)? new ClientesDAO():this.objeto;
        this.setTitle("Registrar Producto");
        this.setScene(escena);
        this.show();
    }

    private void cargar_imagen(){
        FileChooser escoger= new FileChooser();
        escoger.setTitle("Seleecionar Imagen");
        escoger.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagenes","*.png","*.jpg","*jpeg","*.gif")
        );

        File selectedFile = escoger.showOpenDialog(this);
        if (selectedFile != null) {
            try {
                // Mostrar la imagen en el ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imagenView.setImage(image);

                this.rutaIMAGEN=selectedFile.getName();
                Path destinoCarpeta = Paths.get("imagenes");
                Files.createDirectories(destinoCarpeta);

                Path destinoArchivo = destinoCarpeta.resolve(this.rutaIMAGEN);
                Files.copy(selectedFile.toPath(), destinoArchivo, StandardCopyOption.REPLACE_EXISTING);

                String rutaRelativa= "imagenes/"+rutaIMAGEN;
                rutaIMAGEN=rutaRelativa;
                System.out.println("Imagen guardada en: " + rutaIMAGEN);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void CREAR_GUI(){
        this.boton_cargar= new Button("Cargar");
        this.imagenView= new ImageView();
        imagenView.setFitHeight(70);
        imagenView.setFitWidth(70);
        imagenView.setPreserveRatio(true);
        this.boton_cargar.setOnAction(event->{
            cargar_imagen();
        });
        this.salir=new Button("Cancelar");
        this.title =new Label(this.title_);



        txtnombre=new Label("Nombre:");
        nombre=new TextField();
        txtprecio=new Label("Precio:");
        precio=new TextField();
        txtdescripcion=new Label("Descripcion:");
        descripcion=new TextField();
        txtidcategoria=new Label("ID_Categoria:");
        idcategoria=new TextField();

        salir.setOnAction(event->{
            this.close();
        });
        enviar=new Button("Guardar");
        enviar.setOnAction(event->{

            //
            Categoria selecion = comboBox.getSelectionModel().getSelectedItem();
            if(selecion==null){
                objeto.setId_categoria(13);
            }
            else{
                objeto.setId_categoria(selecion.id_categoria);
            }
            objeto.setNombre(nombre.getText());
            objeto.setPrecio(Double.parseDouble(precio.getText()));
            objeto.setDescripcion(descripcion.getText());

            if(rutaIMAGEN.isEmpty() || rutaIMAGEN.length()>50){
                rutaIMAGEN="imagenes/categorias.png";
                objeto.setImagen(rutaIMAGEN);
            }
            else{
                objeto.setImagen(rutaIMAGEN);
            }
            //

            if(option==true){
                objeto.UPDATE();
            }
            else{
                objeto.INSERT();
            }

            //ACTUALIZAR LA TABLA DE CLIENTES
            this.tabla_productos.setItems(objeto.SELECT());
            this.tabla_productos.refresh();
            this.close();
            //
        });


        contenedor_padre=new VBox(imagenView,boton_cargar,title,txtnombre,nombre,txtprecio,precio,txtdescripcion,descripcion,txtidcategoria,comboBox,enviar,salir);
        contenedor_padre.setSpacing(6);
        contenedor_padre.setPadding(new Insets(20));
        contenedor_padre.setAlignment(Pos.CENTER);
        escena=new Scene(contenedor_padre,370,700);
        escena.getStylesheets().add(getClass().getResource("/css/estilo_restaurante.css").toString());

        nombre.getStyleClass().add("display-fondo");
        precio.getStyleClass().add("display-fondo");
        descripcion.getStyleClass().add("display-fondo");
        idcategoria.getStyleClass().add("display-fondo");



        this.enviar.getStyleClass().add("botones-negros");
        this.title.getStyleClass().add("title_space");
        this.salir.getStyleClass().add("botones-rojos");
    }

}



