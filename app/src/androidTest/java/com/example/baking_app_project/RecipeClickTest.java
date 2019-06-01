package com.example.baking_app_project;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeClickTest {
    private IdlingResource idlingResource;
    private final static String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void recipeClickTest(){
        onView(withId(R.id.rv_recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(Matchers.<View>instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(RECIPE_NAME)));
    }

    @After
    public void unregisterIdlingResource(){
        if(idlingResource != null){
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
