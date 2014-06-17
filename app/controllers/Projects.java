package controllers;

import models.Discipline;
import models.Project;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by afv on 25/04/14.
 */
@Security.Authenticated(Secured.class)
public class Projects extends Controller{

    final static Form<Project> projectForm = Form.form(Project.class);

    public static Result all(){
        return ok(
                views.html.Projects.index.render(
                        User.findByEmail(request().username()),
                        Project.all()
                )
        );
    }

    public static Result show(Long id){
        return ok(
                views.html.Projects.item.render(
                        User.findByEmail(request().username()),
                        Project.getById(id),
                        Discipline.all()
                )
        );
    }

    public static Result create(){
        return ok(
                views.html.Projects.item.render(
                        User.findByEmail(request().username()),
                        null,
                        Discipline.all()
                )
        );
    }

    public static Result delete(Long id){
        try{
            Project.delete(id);
            flash("sucesso","Projeto removido com sucesso");
        }catch(Exception e){
            flash("erro", e.getMessage());
        }
        return all();
    }

    public static Result update(Long id){
        Form<Project> filledForm = projectForm.fill(Project.getById(id)).bindFromRequest();
        if(filledForm.hasErrors()){
            flash("erro","Ocorreram erros na gravação" + filledForm.toString());
            return badRequest(views.html.Projects.item.render(User.findByEmail(request().username()),Project.getById(id),Discipline.all()));
        }else{
            Long disciplinaID = Long.parseLong(filledForm.data().get("disciplinaID"));
            Project.update(filledForm.get(), disciplinaID);

            flash("sucesso", "Projeto gravado com sucesso.");
            return redirect(routes.Projects.all());
        }
    }

    public static Result save(){
        Form<Project> filledForm = projectForm.bindFromRequest();
        if(filledForm.hasErrors()){
            flash("erro", "Ocorreram erros na gravação" + filledForm.toString());
            return badRequest(views.html.Projects.item.render(User.findByEmail(request().username()),null,Discipline.all()));
        }else{
            Long disciplinaID = Long.parseLong(filledForm.data().get("disciplinaID"));
            Project.create(filledForm.get(), disciplinaID, User.findByEmail(request().username()).email);

            flash("sucesso", "Projeto gravado com sucesso.");
            return redirect(routes.Projects.all());
        }
    }

}
