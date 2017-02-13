package com.binana.amas.web.rest;

import com.binana.amas.Amas2App;

import com.binana.amas.domain.Asso;
import com.binana.amas.repository.AssoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssoResource REST controller.
 *
 * @see AssoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Amas2App.class)
public class AssoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private AssoRepository assoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAssoMockMvc;

    private Asso asso;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssoResource assoResource = new AssoResource();
        ReflectionTestUtils.setField(assoResource, "assoRepository", assoRepository);
        this.restAssoMockMvc = MockMvcBuilders.standaloneSetup(assoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asso createEntity(EntityManager em) {
        Asso asso = new Asso()
                .name(DEFAULT_NAME);
        return asso;
    }

    @Before
    public void initTest() {
        asso = createEntity(em);
    }

    @Test
    @Transactional
    public void createAsso() throws Exception {
        int databaseSizeBeforeCreate = assoRepository.findAll().size();

        // Create the Asso

        restAssoMockMvc.perform(post("/api/assos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(asso)))
            .andExpect(status().isCreated());

        // Validate the Asso in the database
        List<Asso> assoList = assoRepository.findAll();
        assertThat(assoList).hasSize(databaseSizeBeforeCreate + 1);
        Asso testAsso = assoList.get(assoList.size() - 1);
        assertThat(testAsso.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAssoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assoRepository.findAll().size();

        // Create the Asso with an existing ID
        Asso existingAsso = new Asso();
        existingAsso.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssoMockMvc.perform(post("/api/assos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAsso)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Asso> assoList = assoRepository.findAll();
        assertThat(assoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAssos() throws Exception {
        // Initialize the database
        assoRepository.saveAndFlush(asso);

        // Get all the assoList
        restAssoMockMvc.perform(get("/api/assos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asso.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAsso() throws Exception {
        // Initialize the database
        assoRepository.saveAndFlush(asso);

        // Get the asso
        restAssoMockMvc.perform(get("/api/assos/{id}", asso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(asso.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAsso() throws Exception {
        // Get the asso
        restAssoMockMvc.perform(get("/api/assos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsso() throws Exception {
        // Initialize the database
        assoRepository.saveAndFlush(asso);
        int databaseSizeBeforeUpdate = assoRepository.findAll().size();

        // Update the asso
        Asso updatedAsso = assoRepository.findOne(asso.getId());
        updatedAsso
                .name(UPDATED_NAME);

        restAssoMockMvc.perform(put("/api/assos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAsso)))
            .andExpect(status().isOk());

        // Validate the Asso in the database
        List<Asso> assoList = assoRepository.findAll();
        assertThat(assoList).hasSize(databaseSizeBeforeUpdate);
        Asso testAsso = assoList.get(assoList.size() - 1);
        assertThat(testAsso.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAsso() throws Exception {
        int databaseSizeBeforeUpdate = assoRepository.findAll().size();

        // Create the Asso

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAssoMockMvc.perform(put("/api/assos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(asso)))
            .andExpect(status().isCreated());

        // Validate the Asso in the database
        List<Asso> assoList = assoRepository.findAll();
        assertThat(assoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAsso() throws Exception {
        // Initialize the database
        assoRepository.saveAndFlush(asso);
        int databaseSizeBeforeDelete = assoRepository.findAll().size();

        // Get the asso
        restAssoMockMvc.perform(delete("/api/assos/{id}", asso.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Asso> assoList = assoRepository.findAll();
        assertThat(assoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
