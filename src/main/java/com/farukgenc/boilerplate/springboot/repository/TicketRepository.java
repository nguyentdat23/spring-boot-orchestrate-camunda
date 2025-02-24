package com.farukgenc.boilerplate.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farukgenc.boilerplate.springboot.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
