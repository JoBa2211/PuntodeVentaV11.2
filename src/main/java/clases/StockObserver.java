package clases;

public interface StockObserver {
    void onStockBajoMinimo(Producto producto);
    void onSinStock(Producto producto);
}
