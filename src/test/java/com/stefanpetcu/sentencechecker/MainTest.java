package com.stefanpetcu.sentencechecker;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class MainTest {
    private final InputStream originalInputSource = System.in;
    private final PrintStream originalOutputDestination = System.out;
    private final PrintStream originalErrOutputDestination = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errOutputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errOutputStreamCaptor));
    }

    @After
    public void tearDown() {
        System.setIn(originalInputSource);
        System.setOut(originalOutputDestination);
        System.setErr(originalErrOutputDestination);
    }

    @Test(timeout = 1000)
    public void main_willReturnUserSentence_givenUserInputsValidSentence() {
        System.setIn(new ByteArrayInputStream("Hi there!".getBytes()));
        Main.main(new String[]{});

        assertThat(outputStreamCaptor.toString(),
                equalTo("Please enter your sentence: Hi there!\n"));
    }

    @Test(timeout = 1000)
    @Parameters({
            "HI there!, Sentence should not start with 2 capital letters.",
            "hi there!, Sentence should not start with 2 lowercase letters.",
            "1 cat., Sentence should only start with letters.",
            "... cat., Sentence should only start with letters.",
    })
    public void main_willReturnInvalidSentence_givenUserInputStartsWithInvalidSequences(String userInput, String message) {
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Main.main(new String[]{});

        assertThat(message, errOutputStreamCaptor.toString(),
                equalTo("Invalid sentence. Hint: watch your capitalisation.\n"));
    }

    @Test(timeout = 1000)
    @Parameters({"Hi 3 cats!", "Hi ] there!", "Hi /!", "Hi  there!", "Hi ; there!", "Hi there !", "Hi   there!"})
    // JUnitParams doesn't like using "Hi , there" space padded commas.
    public void main_willReturnInvalidSentence_givenUserInputContainsInvalidCharactersOrSpaces(String userInput) {
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Main.main(new String[]{});

        assertThat(errOutputStreamCaptor.toString(),
                equalTo("Invalid sentence. Hint: see the README file for allowed characters and watch your spaces.\n"));
    }

    @Test(timeout = 1000)
    @Parameters({"Hi! Mate!", "Hi. mate."})
    public void main_willReturnInvalidSentence_givenUserInputContainsMultipleSentences(String userInput) {
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Main.main(new String[]{});

        assertThat(errOutputStreamCaptor.toString(),
                equalTo("Invalid sentence. Hint: watch your punctuation, one sentence only.\n"));
    }

    @Test(timeout = 1000)
    public void main_willReturnInvalidSentence_givenUserInputDoesNotEndProperly() {
        System.setIn(new ByteArrayInputStream("Hi there".getBytes()));
        Main.main(new String[]{});

        assertThat(errOutputStreamCaptor.toString(),
                equalTo("Invalid sentence. Hint: should end properly.\n"));
    }
}
