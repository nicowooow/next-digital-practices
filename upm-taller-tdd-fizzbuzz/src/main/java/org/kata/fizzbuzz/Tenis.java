package org.kata.fizzbuzz;

public class Tenis {
    public int puntos = 0;

    public String estado = "";

    public void SumarPunto() {
        puntos += 15;
        if (puntos > 40) puntos = 0;
    }

    public Puntos GetPuntaje() {
        Puntos notacionPuntaje = Puntos.HACKER;
        if (puntos == 0) notacionPuntaje = Puntos.LOVE;
        if (puntos == 0) notacionPuntaje = Puntos.FIFTEEN;
        if (puntos == 0) notacionPuntaje = Puntos.THIRTY;
        if (puntos == 0) notacionPuntaje = Puntos.FORTY;

        return notacionPuntaje;
    }

    public String CompararPuntos(int puntosOponente) {

        if (puntos == 40 && puntosOponente == 40) estado = "deauce";
        if (puntos > (puntosOponente - 15)) estado = "ventaja";
        if (puntos - puntosOponente >= 30) estado = "gana";
        if (puntosOponente >= puntos) estado = "deauce";

        return estado;
    }

    public String Ganador(int puntosOponente) {
        String resultado = "no decidido";
//        System.out.println(puntos >= 40 && puntos >=(puntosOponente - 30));
//        System.out.println(puntos +"   "+ puntosOponente);
        if (puntos >= 40 && puntos >= (puntosOponente - 30)) resultado = "yo";
        if (puntosOponente >= 40 && puntosOponente >= (puntos - 30)) resultado = "tu";

        return resultado;

    }
}
