package com.binana.amas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.binana.amas.domain.Personn;

import com.binana.amas.repository.PersonnRepository;
import com.binana.amas.web.rest.util.HeaderUtil;
import com.binana.amas.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Personn.
 */
@RestController
@RequestMapping("/api")
public class PersonnResource {

    private final Logger log = LoggerFactory.getLogger(PersonnResource.class);
        
    @Inject
    private PersonnRepository personnRepository;

    /**
     * POST  /personns : Create a new personn.
     *
     * @param personn the personn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personn, or with status 400 (Bad Request) if the personn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personns")
    @Timed
    public ResponseEntity<Personn> createPersonn(@Valid @RequestBody Personn personn) throws URISyntaxException {
        log.debug("REST request to save Personn : {}", personn);
        if (personn.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personn", "idexists", "A new personn cannot already have an ID")).body(null);
        }
        Personn result = personnRepository.save(personn);
        return ResponseEntity.created(new URI("/api/personns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personn", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personns : Updates an existing personn.
     *
     * @param personn the personn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personn,
     * or with status 400 (Bad Request) if the personn is not valid,
     * or with status 500 (Internal Server Error) if the personn couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personns")
    @Timed
    public ResponseEntity<Personn> updatePersonn(@Valid @RequestBody Personn personn) throws URISyntaxException {
        log.debug("REST request to update Personn : {}", personn);
        if (personn.getId() == null) {
            return createPersonn(personn);
        }
        Personn result = personnRepository.save(personn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personn", personn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personns : get all the personns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of personns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/personns")
    @Timed
    public ResponseEntity<List<Personn>> getAllPersonns(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Personns");
        Page<Personn> page = personnRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /personns/:id : get the "id" personn.
     *
     * @param id the id of the personn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personn, or with status 404 (Not Found)
     */
    @GetMapping("/personns/{id}")
    @Timed
    public ResponseEntity<Personn> getPersonn(@PathVariable Long id) {
        log.debug("REST request to get Personn : {}", id);
        Personn personn = personnRepository.findOne(id);
        return Optional.ofNullable(personn)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personns/:id : delete the "id" personn.
     *
     * @param id the id of the personn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personns/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonn(@PathVariable Long id) {
        log.debug("REST request to delete Personn : {}", id);
        personnRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personn", id.toString())).build();
    }

}
