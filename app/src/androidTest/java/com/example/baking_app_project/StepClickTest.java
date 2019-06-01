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
public class StepClickTest {
    private IdlingResource idlingResource;
    private final static String RECIPE_NAME = "Nutella Pie";
    private final static int RECIPE_ID = 0;
    private final static String STEP_DESCRIPTION = "Recipe Introduction";
    // STEP_ID numbering doesn't start at 0
    // not all items in recycler view are clickable, id 0 corresponds with "ingredients" label,
    // then we have all ingredients ids and "steps" label id
    private final static int STEP_ID = 11;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void stepClickTest(){
        onView(withId(R.id.rv_recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_ID, click()));
        onView(withId(R.id.rv_steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(STEP_ID, click()));
        onView(allOf(Matchers.<View>instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(RECIPE_NAME)));
        onView(withId(R.id.step_description)).check(matches(withText(STEP_DESCRIPTION)));
    }

    @After
    public void unregisterIdlingResource(){
        if(idlingResource != null){
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}