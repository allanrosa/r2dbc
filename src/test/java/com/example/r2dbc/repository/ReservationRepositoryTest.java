package com.example.r2dbc.repository;

import com.example.r2dbc.model.Reservation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository repository;

    @Test
    public void all() {

        Flux<Void> deleteAll = repository.findAll().flatMap(r -> this.repository.deleteById(r.getId()));

        StepVerifier.create(deleteAll).expectNextCount(0).verifyComplete();

        Flux<Reservation> reservationFlux = Flux.just("first", "second", "third")
                .map(name -> new Reservation(null, name))
                .flatMap(r -> this.repository.saveReservation(r));

        StepVerifier.create(reservationFlux).expectNextCount(3).verifyComplete();

        Flux<Reservation> allReservation = repository.findAll();

        StepVerifier.create(allReservation).expectNextCount(3).verifyComplete();


    }


}