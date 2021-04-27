package com.li.oop.service.impl;

import com.li.oop.entity.Ticket;
import com.li.oop.repository.TicketRepository;
import com.li.oop.service.intf.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> listTickets() {
        return (List<Ticket>) ticketRepository.findAll();
    }
}
