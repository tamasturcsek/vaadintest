package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("valo")
public class TurcsekUI extends UI {

    private BeanFieldGroup<User> fg = new BeanFieldGroup<>(User.class);

    @PropertyId(User.NAME)
    private TextField nameField;
    @PropertyId("password")
    private PasswordField passwordField;

    @Autowired
    private UserRepo repo;
    private Table table;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setSizeFull();
        FormLayout f = new FormLayout();
        f.setSizeFull();

        f.setMargin(true);
        f.setSpacing(true);
        nameField = new TextField("Name");
        nameField.setNullRepresentation("");
        f.addComponent(nameField);
        passwordField = new PasswordField("Password");
        passwordField.setNullRepresentation("");
        f.addComponent(passwordField);
        f.addComponent(new Button("OK", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    fg.commit();
                    repo.save(fg.getItemDataSource().getBean());
                    fg.setItemDataSource(new User());
                    refresh();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }
            }
        }));

        table = buildUserTable();
        f.addComponent(table);

        fg.setItemDataSource(new User());
        fg.bindMemberFields(this);
refresh();
        setContent(f);
    }

    private Table buildUserTable() {
        Table t = new Table("Felhasználók");

        return t;
    }

    private void refresh() {
        table.setContainerDataSource(new BeanItemContainer<User>(User.class, repo.findAll()));
        table.setColumnHeader("name", "Név");
    }
}
