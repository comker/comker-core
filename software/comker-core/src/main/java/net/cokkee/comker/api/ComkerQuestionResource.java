package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.exception.ComkerObjectDuplicatedException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.po.ComkerQuestion;
import net.cokkee.comker.model.po.ComkerQuestion;
import net.cokkee.comker.service.ComkerStorageManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Api(value = "/question", description = "Question API")
@javax.ws.rs.Path("/question")
public class ComkerQuestionResource {

    private static Log log = LogFactory.getLog(ComkerQuestionResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    private ComkerStorageManager storageManager = null;

    public ComkerStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(ComkerStorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of questions",
        notes = "Returns list of questions",
        response = ComkerQuestion.class,
        responseContainer = "List")
    public Response getQuestionList() {
        return Response.ok(getStorageManager().getQuestionList()).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find question by ID",
        notes = "Returns a question by ID (UUID)",
    response = ComkerQuestion.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Question not found")})
    public Response getQuestionItem(
            @ApiParam(value = "ID of question that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        List<ComkerQuestion> questions = getStorageManager().getQuestionList();
        for(ComkerQuestion question: questions) {
            if (question.getId().equals(id)) {
                return Response.ok().entity(question).build();
            }
        }
        throw new ComkerObjectNotFoundException(404, "Question not found");
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update an existing question")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Question not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response updateQuestionItem(
            @ApiParam(value = "ID of question that needs to be updated", required = true)
            @javax.ws.rs.PathParam("id") 
                String id,
            @ApiParam(value = "Question object that needs to be updated", required = true)
                ComkerQuestion item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Put Question with id:{0}", new Object[] {id}));
        }

        List<ComkerQuestion> questions = getStorageManager().getQuestionList();
        ComkerQuestion current = null;
        for(ComkerQuestion question: questions) {
            if (question.getId().equals(id)) {
                current = question;
                break;
            }
        }
        if (current == null || !id.equals(item.getId()))
            return Response.ok().entity(ComkerQuestion.BAD).build();

        current.setContent(item.getContent());
        current.setExplanation(item.getExplanation());

        current.getAnswerList().clear();
        current.getAnswerList().addAll(item.getAnswerList());
        
        return Response.ok().entity(current).build();
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Add a new question to the store")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createQuestionItem(
            @ApiParam(value = "Question object that needs to be added to the store", required = true)
                ComkerQuestion item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create Question with ID:{0}", new Object[] {item.getId()}));
        }

        List<ComkerQuestion> questions = getStorageManager().getQuestionList();
        for(ComkerQuestion question: questions) {
            if (question.getContent().equals(item.getContent())) {
                throw new ComkerObjectDuplicatedException(409, "Question is duplicated");
            }
        }

        item.setId(UUID.randomUUID().toString());
        questions.add(item);

        item.initStatus().setCode(0);
        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete an existing question")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Question not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response deleteQuestionItem(
            @ApiParam(value = "ID of question that needs to be deleted", required = true)
            @javax.ws.rs.PathParam("id") String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete Question with ID:{0}", new Object[] {id}));
        }

        List<ComkerQuestion> questions = getStorageManager().getQuestionList();
        for(ComkerQuestion question: questions) {
            if (question.getId().equals(id)) {
                questions.remove(question);
                return Response.ok().entity(question).build();
            }
        }

        throw new ComkerObjectNotFoundException(404, "Question not found");
    }
}
