package hn.optica.model;

public class Medicion {

    private int id;
    private int clienteId;
    private String clienteNombre;
    private String fecha;
    private double esferaOd;
    private double cilindroOd;
    private int ejeOd;
    private double esferaOi;
    private double cilindroOi;
    private int ejeOi;
    private double adicion;
    private double distanciaPupilar;
    private String observaciones;

    public Medicion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getEsferaOd() {
        return esferaOd;
    }

    public void setEsferaOd(double esferaOd) {
        this.esferaOd = esferaOd;
    }

    public double getCilindroOd() {
        return cilindroOd;
    }

    public void setCilindroOd(double cilindroOd) {
        this.cilindroOd = cilindroOd;
    }

    public int getEjeOd() {
        return ejeOd;
    }

    public void setEjeOd(int ejeOd) {
        this.ejeOd = ejeOd;
    }

    public double getEsferaOi() {
        return esferaOi;
    }

    public void setEsferaOi(double esferaOi) {
        this.esferaOi = esferaOi;
    }

    public double getCilindroOi() {
        return cilindroOi;
    }

    public void setCilindroOi(double cilindroOi) {
        this.cilindroOi = cilindroOi;
    }

    public int getEjeOi() {
        return ejeOi;
    }

    public void setEjeOi(int ejeOi) {
        this.ejeOi = ejeOi;
    }

    public double getAdicion() {
        return adicion;
    }

    public void setAdicion(double adicion) {
        this.adicion = adicion;
    }

    public double getDistanciaPupilar() {
        return distanciaPupilar;
    }

    public void setDistanciaPupilar(double distanciaPupilar) {
        this.distanciaPupilar = distanciaPupilar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
