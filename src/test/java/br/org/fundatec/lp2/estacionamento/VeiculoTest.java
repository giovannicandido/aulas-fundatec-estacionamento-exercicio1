package br.org.fundatec.lp2.estacionamento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class VeiculoTest {

    @ParameterizedTest
    @MethodSource("provideVeiculos")
    void calcularHoraZeroOuNegativa(Veiculo veiculo) {
        assertThatThrownBy(() -> {
            veiculo.calcularValorHora(0);
        }).isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> {
            veiculo.calcularValorHora(-1);
        }).isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @MethodSource("provideVeiculos")
    void calcularValorHoraAteMeiaHora(Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(0.2)).isEqualTo(veiculo.valorMeiaHora);
        assertThat(veiculo.calcularValorHora(0.5)).isEqualTo(veiculo.valorMeiaHora);
    }

    @ParameterizedTest
    @MethodSource("provideVeiculos")
    void calcularValorEntreMeiaHora1(Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(1)).isEqualTo(veiculo.valorAteUmaHora);
        assertThat(veiculo.calcularValorHora(0.7)).isEqualTo(veiculo.valorAteUmaHora);
    }

    @ParameterizedTest
    @MethodSource("provideHorasEntre2E6Inteiros")
    void valorEntre2HoraE6HorasInteiros(double hora, double expected, Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(hora)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideHorasEntre2e6Quebradas")
    void valorEntre2HoraE6HorasQuebrados(double hora, double expected, Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(hora)).isEqualTo(expected);
    }


    @ParameterizedTest
    @MethodSource("provideVeiculos")
    void valorMaiorQue6HorasMenorQue24Horas(Veiculo veiculo) {
        IntStream.range(7, 25).forEach(integer -> {
            assertThat(veiculo.calcularValorHora(integer)).isEqualTo(veiculo.valorDiaria);
        });
        assertThat(veiculo.calcularValorHora(6.5)).isEqualTo(veiculo.valorDiaria);
    }

    @ParameterizedTest
    @MethodSource("provideVeiculos")
    void valorMaiorQue24HorasAteMeiaHora(Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(24.5)).isEqualTo(veiculo.valorDiaria + veiculo.valorMeiaHora);
    }

    @ParameterizedTest
    @MethodSource("provideHorasEntre2E6Inteiros")
    void valorMaiorQue24HorasEntre2HoraE6HorasDiaInteiros(double hora, double expected, Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(24 + hora)).isEqualTo(veiculo.valorDiaria + expected);
    }

    @ParameterizedTest
    @MethodSource("provideVeiculos")
    void valorMaior24HorasAdicionalEntre6E24Horas(Veiculo veiculo) {
        IntStream.range(31, 48).forEach(integer -> {
            assertThat(veiculo.calcularValorHora(integer)).isEqualTo(veiculo.valorDiaria * 2);
        });
    }

    @ParameterizedTest
    @MethodSource("provideDiasHoras")
    void valorHorasAte30DiasMaisUmDia(int dias, double horas, Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(dias * 24 + horas))
                .isEqualTo(veiculo.valorDiaria * dias + horas * veiculo.valorHoraAdicional);
    }

    @ParameterizedTest
    @MethodSource("provideDiasHorasMaiorQue6")
    void valorHorasAte30DiasMaisUmDiaMaiorQue6(int dias, double horas, Veiculo veiculo) {
        assertThat(veiculo.calcularValorHora(dias * 24 + horas))
                .isEqualTo(veiculo.valorDiaria * (dias + 1));
    }


    private static Stream<Arguments> provideVeiculos() {
        return Stream.of(
                Arguments.of(new Carro()),
                Arguments.of(new Moto()),
                Arguments.of(new Bicicleta())
        );
    }

    private static Stream<Arguments> provideHorasEntre2E6Inteiros() {
        return Stream.of(
                Arguments.of(2, 20, new Carro()),
                Arguments.of(3, 25, new Carro()),
                Arguments.of(4, 30, new Carro()),
                Arguments.of(5, 35, new Carro()),
                Arguments.of(6, 40, new Carro()),
                Arguments.of(2, 13, new Moto()),
                Arguments.of(3, 16, new Moto()),
                Arguments.of(4, 19, new Moto()),
                Arguments.of(5, 22, new Moto()),
                Arguments.of(6, 25, new Moto()),
                Arguments.of(2, 9, new Bicicleta()),
                Arguments.of(3, 11, new Bicicleta()),
                Arguments.of(4, 13, new Bicicleta()),
                Arguments.of(5, 15, new Bicicleta()),
                Arguments.of(6, 17, new Bicicleta())
        );
    }

    private static Stream<Arguments> provideHorasEntre2e6Quebradas() {
        return Stream.of(
                Arguments.of(2.5, 22.5, new Carro()),
                Arguments.of(3.5, 27.5, new Carro()),
                Arguments.of(5.2, 36, new Carro()),
                Arguments.of(2.5, 14.5, new Moto()),
                Arguments.of(3.5, 17.5, new Moto()),
                Arguments.of(5.2, 22.6, new Moto()),
                Arguments.of(2.5, 10, new Bicicleta()),
                Arguments.of(3.5, 12, new Bicicleta()),
                Arguments.of(5.2, 15.4, new Bicicleta())
        );
    }

    private static Stream<Arguments> provideDiasHoras() {
        List<Arguments> argumentsList = new ArrayList<>();

        IntStream.range(1, 4).forEach(integer -> {;
            argumentsList.add(Arguments.of(integer, 5, new Moto()));
            argumentsList.add(Arguments.of(integer, 5, new Carro()));
            argumentsList.add(Arguments.of(integer, 3, new Bicicleta()));
        });
        Arguments[] arguments = new Arguments[argumentsList.size()];
        for (int i = 0; i < argumentsList.size(); i++) {
            arguments[i] = argumentsList.get(i);
        }

        return Arrays.stream(arguments);
    }

    private static Stream<Arguments> provideDiasHorasMaiorQue6() {
        List<Arguments> argumentsList = new ArrayList<>();

        IntStream.range(1, 4).forEach(integer -> {;
            argumentsList.add(Arguments.of(integer, 7, new Moto()));
            argumentsList.add(Arguments.of(integer, 6.5, new Moto()));
            argumentsList.add(Arguments.of(integer, 8, new Carro()));
            argumentsList.add(Arguments.of(integer, 24, new Bicicleta()));
        });
        Arguments[] arguments = new Arguments[argumentsList.size()];
        for (int i = 0; i < argumentsList.size(); i++) {
            arguments[i] = argumentsList.get(i);
        }

        return Arrays.stream(arguments);
    }





}