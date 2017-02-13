package com.binana.amas.web.rest;

import com.binana.amas.Amas2App;

import com.binana.amas.domain.Personn;
import com.binana.amas.repository.PersonnRepository;

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
 * Test class for the PersonnResource REST controller.
 *
 * @see PersonnResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Amas2App.class)
public class PersonnResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private PersonnRepository personnRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPersonnMockMvc;

    private Personn personn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonnResource personnResource = new PersonnResource();
        ReflectionTestUtils.setField(personnResource, "personnRepository", personnRepository);
        this.restPersonnMockMvc = MockMvcBuilders.standaloneSetup(personnResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personn createEntity(EntityManager em) {
        Personn personn = new Personn()
                .name(DEFAULT_NAME);
        return personn;
    }

    @Before
    public void initTest() {
        personn = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonn() throws Exception {
        int databaseSizeBeforeCreate = personnRepository.findAll().size();

        // Create the Personn

        restPersonnMockMvc.perform(post("/api/personns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personn)))
            .andExpect(status().isCreated());

        // Validate the Personn in the database
        List<Personn> personnList = personnRepository.findAll();
        assertThat(personnList).hasSize(databaseSizeBeforeCreate + 1);
        Personn testPersonn = personnList.get(personnList.size() - 1);
        assertThat(testPersonn.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPersonnWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personnRepository.findAll().size();

        // Create the Personn with an existing ID
        Personn existingPersonn = new Personn();
        existingPersonn.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnMockMvc.perform(post("/api/personns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPersonn)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Personn> personnList = personnRepository.findAll();
        assertThat(personnList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personnRepository.findAll().size();
        // set the field null
        personn.setName(null);

        // Create the Personn, which fails.

        restPersonnMockMvc.perform(post("/api/personns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personn)))
            .andExpect(status().isBadRequest());

        List<Personn> personnList = personnRepository.findAll();
        assertThat(personnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonns() throws Exception {
        // Initialize the database
        personnRepository.saveAndFlush(personn);

        // Get all the personnList
        restPersonnMockMvc.perform(get("/api/personns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personn.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPersonn() throws Exception {
        // Initialize the database
        personnRepository.saveAndFlush(personn);

        // Get the personn
        restPersonnMockMvc.perform(get("/api/personns/{id}", personn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personn.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonn() throws Exception {
        // Get the personn
        restPersonnMockMvc.perform(get("/api/personns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonn() throws Exception {
        // Initialize the database
        personnRepository.saveAndFlush(personn);
        int databaseSizeBeforeUpdate = personnRepository.findAll().size();

        // Update the personn
        Personn updatedPersonn = personnRepository.findOne(personn.getId());
        updatedPersonn
                .name(UPDATED_NAME);

        restPersonnMockMvc.perform(put("/api/personns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonn)))
            .andExpect(status().isOk());

        // Validate the Personn in the database
        List<Personn> personnList = personnRepository.findAll();
        assertThat(personnList).hasSize(databaseSizeBeforeUpdate);
        Personn testPersonn = personnList.get(personnList.size() - 1);
        assertThat(testPersonn.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonn() throws Exception {
        int databaseSizeBeforeUpdate = personnRepository.findAll().size();

        // Create the Personn

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersonnMockMvc.perform(put("/api/personns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personn)))
            .andExpect(status().isCreated());

        // Validate the Personn in the database
        List<Personn> personnList = personnRepository.findAll();
        assertThat(personnList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePersonn() throws Exception {
        // Initialize the database
        personnRepository.saveAndFlush(personn);
        int databaseSizeBeforeDelete = personnRepository.findAll().size();

        // Get the personn
        restPersonnMockMvc.perform(delete("/api/personns/{id}", personn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Personn> personnList = personnRepository.findAll();
        assertThat(personnList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
