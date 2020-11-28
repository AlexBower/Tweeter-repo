package edu.byu.cs.tweeter.model.net;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import edu.byu.cs.tweeter.model.domain.User;

public class ImageBytesExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        if (field.getDeclaringClass() == User.class && field.getName().equals("imageBytes")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
};
