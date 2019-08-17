package io.github.mufasa1976.spring.test.softwareday;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mufasa1976.spring.test.softwareday.entities.NoteEntity;
import io.github.mufasa1976.spring.test.softwareday.repositories.NoteRepository;
import io.github.mufasa1976.spring.test.softwareday.resources.NoteResource;
import io.github.mufasa1976.spring.test.softwareday.services.NoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest("debug=true")
@AutoConfigureMockMvc
@Transactional
public class SpringBootTestSoftwaredayApplicationTests {
  private static final LocalDateTime LAST_UPDATED_AT = LocalDateTime.of(2019, 8, 17, 0, 34, 0, 1000000);
  private static final String FORMATTED_LAST_UPDATED_AT = LAST_UPDATED_AT.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

  private static final LocalDateTime NOW = LocalDateTime.now();
  private static final String FORMATTED_NOW = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(NOW);

  @Autowired
  private MockMvc web;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DateTimeProvider dateTimeProvider;

  @Autowired
  private NoteRepository noteRepository;

  @SpyBean
  private NoteService noteService;

  @Before
  public void setUp() {
    when(dateTimeProvider.getNow()).thenReturn(Optional.of(NOW));
  }

  @Test
  @Sql("classpath:/scripts/db/notes.sql")
  public void findAllNotes_OK() throws Exception {
    web.perform(get(Routes.NOTES)
        .param("size", "3")
        .param("page", "1")
        .param("sort", "subject,ASC"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.notes").isArray())
       .andExpect(jsonPath("_embedded.notes.*", hasSize(3)))
       .andExpect(jsonPath("_embedded.notes[0].*", hasSize(5)))
       .andExpect(jsonPath("_embedded.notes[0].reference").value(is("00000000-0000-0000-0000-000000000004")))
       .andExpect(jsonPath("_embedded.notes[0].subject").value(is("Test Note 04")))
       .andExpect(jsonPath("_embedded.notes[0].body").value(is("Body of Test Note 04")))
       .andExpect(jsonPath("_embedded.notes[0].lastUpdatedAt").value(is(FORMATTED_LAST_UPDATED_AT)))
       .andExpect(jsonPath("_embedded.notes[0]._links.self.href").value(is(getSelfLink("00000000-0000-0000-0000-000000000004"))))
       .andExpect(jsonPath("_embedded.notes[1].*", hasSize(5)))
       .andExpect(jsonPath("_embedded.notes[1].reference").value(is("00000000-0000-0000-0000-000000000005")))
       .andExpect(jsonPath("_embedded.notes[1].subject").value(is("Test Note 05")))
       .andExpect(jsonPath("_embedded.notes[1].body").value(is("Body of Test Note 05")))
       .andExpect(jsonPath("_embedded.notes[1].lastUpdatedAt").value(is(FORMATTED_LAST_UPDATED_AT)))
       .andExpect(jsonPath("_embedded.notes[1]._links.self.href").value(is(getSelfLink("00000000-0000-0000-0000-000000000005"))))
       .andExpect(jsonPath("_embedded.notes[2].*", hasSize(5)))
       .andExpect(jsonPath("_embedded.notes[2].reference").value(is("00000000-0000-0000-0000-000000000006")))
       .andExpect(jsonPath("_embedded.notes[2].subject").value(is("Test Note 06")))
       .andExpect(jsonPath("_embedded.notes[2].body").value(is("Body of Test Note 06")))
       .andExpect(jsonPath("_embedded.notes[2].lastUpdatedAt").value(is(FORMATTED_LAST_UPDATED_AT)))
       .andExpect(jsonPath("_embedded.notes[2]._links.self.href").value(is(getSelfLink("00000000-0000-0000-0000-000000000006"))))
       .andExpect(jsonPath("_links.self.href").exists())
       .andExpect(jsonPath("page.size").value(is(3)))
       .andExpect(jsonPath("page.totalElements").value(is(10)))
       .andExpect(jsonPath("page.totalPages").value(is(4)))
       .andExpect(jsonPath("page.number").value(is(1)));
  }

  private String getSelfLink(String reference) {
    return String.format("http://localhost%s/%s", Routes.NOTES, reference);
  }

  @Test
  public void findAllNotes_OK_noDataFound() throws Exception {
    web.perform(get(Routes.NOTES))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.notes").doesNotExist())
       .andExpect(jsonPath("_links.self.href").exists())
       .andExpect(jsonPath("page.size").value(is(20)))
       .andExpect(jsonPath("page.totalElements").value(is(0)))
       .andExpect(jsonPath("page.totalPages").value(is(0)))
       .andExpect(jsonPath("page.number").value(is(0)));
  }

  @Test
  @Sql("classpath:/scripts/db/notes.sql")
  public void findNoteByReference_OK() throws Exception {
    web.perform(get(Routes.NOTE, "00000000-0000-0000-0000-000000000004"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("*", hasSize(5)))
       .andExpect(jsonPath("reference").value(is("00000000-0000-0000-0000-000000000004")))
       .andExpect(jsonPath("subject").value(is("Test Note 04")))
       .andExpect(jsonPath("body").value(is("Body of Test Note 04")))
       .andExpect(jsonPath("lastUpdatedAt").value(is(FORMATTED_LAST_UPDATED_AT)))
       .andExpect(jsonPath("_links.self.href").value(is(getSelfLink("00000000-0000-0000-0000-000000000004"))));
  }

  @Test
  public void findNoteByReference_NOK_noDataFound() throws Exception {
    web.perform(get(Routes.NOTE, "00000000-0000-0000-0000-000000000004"))
       .andExpect(status().isNotFound());
  }

  @Test
  public void create_OK() throws Exception {
    final NoteResource noteResource = objectMapper.readValue(
        web.perform(post(Routes.NOTES)
            .contentType(APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsBytes(
                NoteResource.builder()
                            .subject("Another Test Note")
                            .body("Another Test Note Body")
                            .build())))
           .andExpect(status().isOk())
           .andExpect(content().contentType(HAL_JSON_UTF8))
           .andExpect(jsonPath("*", hasSize(5)))
           .andExpect(jsonPath("reference").exists())
           .andExpect(jsonPath("subject").value(is("Another Test Note")))
           .andExpect(jsonPath("body").value(is("Another Test Note Body")))
           .andExpect(jsonPath("lastUpdatedAt").value(is(FORMATTED_NOW)))
           .andExpect(jsonPath("_links.self.href").exists())
           .andReturn()
           .getResponse()
           .getContentAsByteArray(), NoteResource.class);

    final NoteEntity entity = noteRepository.findOptionalByReference(noteResource.getReference())
                                            .orElseThrow();
    assertThat(entity.getId()).isNotNull()
                              .isPositive();
    assertThat(entity.getReference()).isNotNull();
    assertThat(entity)
        .extracting(
            NoteEntity::getSubject,
            NoteEntity::getBody,
            NoteEntity::getLastUpdatedAt)
        .containsOnly("Another Test Note", "Another Test Note Body", NOW);
  }

  @Test
  public void create_NOK_missingSubject() throws Exception {
    web.perform(post(Routes.NOTES)
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsBytes(
            NoteResource.builder()
                        .body("Another Test Note Body")
                        .build())))
       .andExpect(status().isBadRequest());

    verify(noteService, never()).create(any());
  }

  @Test
  @Sql("classpath:/scripts/db/notes.sql")
  public void update_OK() throws Exception {
    web.perform(put(Routes.NOTE, "00000000-0000-0000-0000-000000000007")
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsBytes(
            NoteResource.builder()
                        .reference(UUID.randomUUID())
                        .subject("Changed Note")
                        .body("Body of the changed Note")
                        .lastUpdatedAt(LAST_UPDATED_AT)
                        .build())))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("*", hasSize(5)))
       .andExpect(jsonPath("reference").value(is("00000000-0000-0000-0000-000000000007")))
       .andExpect(jsonPath("subject").value(is("Changed Note")))
       .andExpect(jsonPath("body").value(is("Body of the changed Note")))
       .andExpect(jsonPath("lastUpdatedAt").value(is(FORMATTED_NOW)))
       .andExpect(jsonPath("_links.self.href").value(is(getSelfLink("00000000-0000-0000-0000-000000000007"))));
  }
}
