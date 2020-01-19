package function;

import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FitnessExampleTest {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;

    private String expectedResultForTestCase =
            "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('7946177096518930734');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img7946177096518930734\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"7946177096518930734\">suiteSetUp</div>\n" +
            "</div>\n" +
            "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('-6181035654133937183');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img-6181035654133937183\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"-6181035654133937183\">setup</div>\n" +
            "</div>\n" +
            "<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('-1597705501149256939');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img-1597705501149256939\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"-1597705501149256939\">suiteTearDown</div>\n" +
            "</div>\n";

    private String expectedResultForNonTestCase =
            "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('5441299761002322147');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img5441299761002322147\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"5441299761002322147\">setup</div>\n" +
            "</div>\n" +
            "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('-8007412913319301290');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img-8007412913319301290\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"-8007412913319301290\">suiteSetUp</div>\n" +
            "</div>\n" +
            "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('7309237215646768218');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img7309237215646768218\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"7309237215646768218\">setup</div>\n" +
            "</div>\n" +
            "<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('4843654200327708517');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img4843654200327708517\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"4843654200327708517\">suiteTearDown</div>\n" +
            "</div>\n" +
            "<div class=\"teardown\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('6152635111714871630');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img6152635111714871630\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Tear Down: <a href=\"TearDown\">.TearDown</a> <a href=\"TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"6152635111714871630\">teardown</div>\n" +
            "</div>\n";
    @Before
    public void setUp() throws Exception {
        root = InMemoryPage.makeRoot("RooT");
        crawler = root.getPageCrawler();
        testPage = addPage("TestPage", "!define TEST_SYSTEM {slim}\n" + "the content");
        addPage("SetUp", "setup");
        addPage("TearDown", "teardown");
        addPage("SuiteSetUp", "suiteSetUp");
        addPage("SuiteTearDown", "suiteTearDown");

        crawler.addPage(testPage, PathParser.parse("ScenarioLibrary"), "scenario library 2");

        pageData = testPage.getData();
    }

    private WikiPage addPage(String pageName, String content) throws Exception {
        return crawler.addPage(root, PathParser.parse(pageName), content);
    }

    private String removeMagicNumber(String expectedResult) {
        return expectedResult.replaceAll("[-]*\\d+", "");
    }

    @Test
    public void testableHtml() throws Exception {
        String testableHtml = new FitnessExample().testableHtml(pageData, true);
        assertThat(removeMagicNumber(testableHtml), is(removeMagicNumber(expectedResultForTestCase)));

        testableHtml = new FitnessExample().testableHtml(pageData, false);
        assertThat(removeMagicNumber(testableHtml), is(removeMagicNumber(expectedResultForNonTestCase)));
    }
}
