package com.example.notemanagment.Models;

public class ModuleFieldCombination {
    private Module module;
    private Field field;

    public ModuleFieldCombination(Module module, Field field) {
        this.module = module;
        this.field = field;
    }

    public Module getModule() {
        return module;
    }

    public Field getField() {
        return field;
    }
}