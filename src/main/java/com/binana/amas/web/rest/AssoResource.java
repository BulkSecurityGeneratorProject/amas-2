package com.binana.amas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.binana.amas.domain.Asso;

import com.binana.amas.repository.AssoRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Asso.
 */
@RestController
@RequestMapping("/api")
public class AssoResource {

    private final Logger log = LoggerFactory.getLogger(AssoResource.class);
        
    @Inject
    private AssoRepository assoRepository;

    /**
     * POST  /assos : Create a new asso.
     *
     * @param asso the asso to create
     * @return the ResponseEntity with status 201 (Created) and with body the new asso, or with status 400 (Bad Request) if the asso has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assos")
    @Timed
    public ResponseEntity<Asso> createAsso(@RequestBody Asso asso) throws URISyntaxException {
        log.debug("REST request to save Asso : {}", asso);
        if (asso.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asso", "idexists", "A new asso cannot already have an ID")).body(null);
        }
        Asso result = assoRepository.save(asso);
        return ResponseEntity.created(new URI("/api/assos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("asso", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assos : Updates an existing asso.
     *
     * @param asso the asso to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated asso,
     * or with status 400 (Bad Request) if the asso is not valid,
     * or with status 500 (Internal Server Error) if the asso couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assos")
    @Timed
    public ResponseEntity<Asso> updateAsso(@RequestBody Asso asso) throws URISyntaxException {
        log.debug("REST request to update Asso : {}", asso);
        if (asso.getId() == null) {
            return createAsso(asso);
        }
        Asso result = assoRepository.save(asso);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("asso", asso.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assos : get all the assos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/assos")
    @Timed
    public ResponseEntity<List<Asso>> getAllAssos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Assos");
        Page<Asso> page = assoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assos/:id : get the "id" asso.
     *
     * @param id the id of the asso to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the asso, or with status 404 (Not Found)
     */
    @GetMapping("/assos/{id}")
    @Timed
    public ResponseEntity<Asso> getAsso(@PathVariable Long id) {
        log.debug("REST request to get Asso : {}", id);
        Asso asso = assoRepository.findOne(id);
        return Optional.ofNullable(asso)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /assos/:id : delete the "id" asso.
     *
     * @param id the id of the asso to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAsso(@PathVariable Long id) {
        log.debug("REST request to delete Asso : {}", id);
        assoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("asso", id.toString())).build();
    }

}
