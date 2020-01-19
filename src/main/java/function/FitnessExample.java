package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample {
    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        return new SetUpTearDownSurrounder(pageData, includeSuiteSetup).surround();
    }

    private class SetUpTearDownSurrounder {
        private PageData pageData;
        private boolean includeSuiteSetup;
        private WikiPage wikiPage;
        private final StringBuffer buffer;

        public SetUpTearDownSurrounder(PageData pageData, boolean includeSuiteSetup) {
            this.pageData = pageData;
            this.includeSuiteSetup = includeSuiteSetup;
            buffer = new StringBuffer();
            wikiPage = pageData.getWikiPage();
        }

        public String surround() throws Exception {
            if (isTestPage())
                surroundPageWithSetUpsAndTearDowns();
            return pageData.getHtml();
        }

        private void surroundPageWithSetUpsAndTearDowns() throws Exception {
            includeSetups();
            buffer.append(pageData.getContent());
            includeTearDowns();
            pageData.setContent(buffer.toString());
        }

        private boolean isTestPage() throws Exception {
            return pageData.hasAttribute("Test");
        }

        private void includeTearDowns() throws Exception {
            includeInherited("TearDown", "teardown");
            if (includeSuiteSetup)
                includeInherited(SuiteResponder.SUITE_TEARDOWN_NAME, "teardown");
        }

        private void includeSetups() throws Exception {
            if (includeSuiteSetup)
                includeInherited(SuiteResponder.SUITE_SETUP_NAME, "setup");
            includeInherited("SetUp", "setup");
        }

        private void includeInherited(String pageName, String mode) throws Exception {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
            if (suiteSetup != null)
                includePage(suiteSetup, mode);
        }

        private void includePage(WikiPage suiteSetup, String mode) throws Exception {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            buffer.append("!include -" + mode + " .").append(pagePathName).append("\n");
        }
    }
}
