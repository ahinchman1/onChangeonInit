package com.example.demo.view

import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.BorderPane
import tornadofx.*

class Person(name: String? = null, title: String? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val titleProperty = SimpleStringProperty(this, "title", title)
    var title by titleProperty
}

class PersonEditor : View("Person Editor") {
    override val root = BorderPane()
    val persons = listOf(Person("John", "Manager"), Person("Jay", "Worker bee")).observable()
    val model: PersonModel by inject()

    init {
        with(root) {
            center {
                tableview(persons) {
                    column("Name", Person::nameProperty)
                    column("Title", Person::titleProperty)
                    bindSelected(model)
                }
            }

            right {
                form {
                    fieldset("Edit person") {
                        field("Name") {
                            textfield(model.name)
                        }
                        field("Title") {
                            textfield(model.title)
                        }
                        button("Save") {
                            enableWhen(model.dirty)
                            action {
                                save()
                            }
                        }
                        button("Reset").action {
                            model.rollback()
                        }
                    }
                }
            }
        }
    }

    private fun save() {
        model.commit()
        val person = model.item
        println("Saving ${person.name} / ${person.title}")
    }

}
class PersonModel : ItemViewModel<Person>() {
    val name = bind(Person::nameProperty)
    val title = bind(Person::titleProperty)
}