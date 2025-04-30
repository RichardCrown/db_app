package com.example.demo2.vistas;

import com.example.demo2.modulos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;

public class insumos extends Stage {



    private Scene escena;

    private Label txtnombre,txtcantidad,txtdescripcion,txtobservaciones,txtidproveedor;
    private VBox contenedor_padre;
    private Button enviar;

    private TextField nombre;
    private TextField cantidad;
    private TextField descripcion;
    private TextField observaciones;
    private TextField idproveedor;


    private InsumosDAO objeto;
    private Label title;
    private Button salir;
    private boolean option;
    private String title_;

    private TableView<InsumosDAO> tabla_insumos;

    private ProveedoresDAO cat_proveedores;
    List<Proveedores> lista_de_proveedores;

    ObservableList<Proveedores> observable_lista;
    ComboBox<Proveedores> comboBox;

    public insumos(TableView<InsumosDAO> tabla_insumos,InsumosDAO objeto_dao,String title_){
        cat_proveedores = new ProveedoresDAO();
        lista_de_proveedores= cat_proveedores.Obtener_Proveedores();

        observable_lista= FXCollections.observableArrayList(lista_de_proveedores);
        comboBox=new ComboBox<>(observable_lista);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Proveedores proveedor_a){
                return (proveedor_a!=null)?proveedor_a.getNombre():"";
            }

            @Override
            public Proveedores fromString(String string) {
                return null;
            }
        });


        this.title_=title_;
        this.tabla_insumos=tabla_insumos;
        CREAR_GUI();
        if(objeto_dao==null){
            objeto =new InsumosDAO();
            option=false;
        }
        else{
            option=true;
            objeto=objeto_dao;


            this.nombre.setText(objeto.getNombre());
            this.cantidad.setText(String.valueOf(objeto.getCantidad()));
            this.descripcion.setText(objeto.getDescripcion());
            this.observaciones.setText(objeto.getObservaciones());
        }
        //objeto = (objeto_dao==null)? new ClientesDAO():this.objeto;
        this.setTitle("Registrar Insumo");
        this.setScene(escena);
        this.show();
    }

    private void CREAR_GUI(){
        this.salir=new Button("Cancelar");
        this.title =new Label(this.title_);


        txtnombre=new Label("Nombre:");
        nombre=new TextField();
        txtcantidad=new Label("Cantidad:");
        cantidad=new TextField();
        txtdescripcion=new Label("Descripcion:");
        descripcion=new TextField();
        txtobservaciones=new Label("Observaciones:");
        observaciones=new TextField();
        txtidproveedor=new Label("Id Proveedor:");
        idproveedor=new TextField();


        salir.setOnAction(event->{
            this.close();
        });
        enviar=new Button("Guardar");
        enviar.setOnAction(event->{


            //
            Proveedores selecion = comboBox.getSelectionModel().getSelectedItem();
            if(selecion==null){
                try{
                    objeto.setId_proveedor(1);
                }
                catch (Exception e){
                    new InformeGeneral("No se puede crear no existe El Proveedor");
                }
            }
            else{
                objeto.setId_proveedor(selecion.getId_proveedor());
            }
            objeto.setNombre(nombre.getText());
            objeto.setCantidad(Integer.parseInt(cantidad.getText()));
            objeto.setDescripcion(descripcion.getText());
            objeto.setObservaciones(observaciones.getText());
            //

            if(option==true){
                objeto.UPDATE();
            }
            else{
                objeto.INSERT();
            }

            //ACTUALIZAR LA TABLA DE CLIENTES
            this.tabla_insumos.setItems(objeto.SELECT());
            this.tabla_insumos.refresh();
            this.close();
            //
        });

        contenedor_padre=new VBox(title,txtnombre,nombre,txtcantidad,cantidad,txtdescripcion,descripcion,txtobservaciones,observaciones,txtidproveedor,comboBox,enviar,salir);
        contenedor_padre.setSpacing(5);
        contenedor_padre.setPadding(new Insets(20));
        contenedor_padre.setAlignment(Pos.CENTER);
        escena=new Scene(contenedor_padre,370,700);
        escena.getStylesheets().add(getClass().getResource("/css/estilo_restaurante.css").toString());

        nombre.getStyleClass().add("display-fondo");
        cantidad.getStyleClass().add("display-fondo");
        descripcion.getStyleClass().add("display-fondo");
        observaciones.getStyleClass().add("display-fondo");
        idproveedor.getStyleClass().add("display-fondo");

        enviar.getStyleClass().add("botones-negros");
        this.title.getStyleClass().add("title_space");
        this.salir.getStyleClass().add("botones-rojos");
    }

}



