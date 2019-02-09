package com.example.r2dbc.repository;

import com.example.r2dbc.model.Reservation;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ReservationRepository {

    private final ConnectionFactory connectionFactory;

    public ReservationRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Mono<Void> deleteById(Integer id){
        return this.connectionMono()
                .flatMapMany(connection -> connection.createStatement("delete from reservation where id = $1")
                        .bind("$1", id)
                        .execute()
                ).then();
    }

    public Flux<Reservation> findAll(){
        return connectionMono().flatMapMany(connection ->
                Flux
                        .from(connection.createStatement("select * from reservation").execute())
                        .flatMap(r -> r.map((row, rowMetadata) -> new Reservation(
                                row.get("id", Integer.class),
                                row.get("name", String.class)
                                )))
                );
    }

    public Flux<Reservation> saveReservation(Reservation reservation){
        Flux<? extends Result> flatMapMany =  this.connectionMono()
                .flatMapMany(connection -> connection.createStatement("insert into reservation (name) values ($1)")
                        .bind("$1", reservation.getName())
                        .add()
                        .execute()
                );
        return flatMapMany
                .switchMap(result -> Flux.just(new Reservation(reservation.getId(), reservation.getName()))
                );
    }

    private Mono<Connection> connectionMono(){
        return Mono.from(this.connectionFactory.create());
    }
}
