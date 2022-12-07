package br.org.fundatec.lp2.estacionamento;

public abstract class Veiculo {
    protected final double valorMeiaHora;
    protected final double valorAteUmaHora;
    protected final double valorHoraAdicional;
    protected final double valorDiaria;

    public Veiculo(double valorMeiaHora, double valorAteUmaHora, double valorHoraAdicional, double valorDiaria) {
        this.valorMeiaHora = valorMeiaHora;
        this.valorAteUmaHora = valorAteUmaHora;
        this.valorHoraAdicional = valorHoraAdicional;
        this.valorDiaria = valorDiaria;
    }

    public double calcularValorHora(double tempoEstacionamento) {
        if(tempoEstacionamento <= 0) {
            throw new RuntimeException("Tempo de estacionamento não pode ser menor ou igual a zero");
        }
        if(tempoEstacionamento <= 0.5) {
            return valorMeiaHora;
        }

        if(tempoEstacionamento > 0.5 && tempoEstacionamento <=1) {
            return valorAteUmaHora;
        }

        if(tempoEstacionamento > 1 && tempoEstacionamento <=6) {
            return valorAteUmaHora + (tempoEstacionamento - 1) * valorHoraAdicional;
        }

        if(tempoEstacionamento > 6) {
            return valorDiaria;
        }

        return 0;
    }
}
