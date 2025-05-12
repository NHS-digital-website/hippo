Feature: Ensure Glossary page displays required fields.


    Scenario: Check individual content fields (Glossary)
        Given I navigate to the "Glossary test document" page
        Then I should see glossary page titled "Glossary AcceptanceTestDocument"
        Then I should see glossary page summary "Glossary Lorem ipsum dolor sit amet, consectetur ..."
        And I should also see:
            | Glossary List         | B\nBananas\nA banana is an edible fruit ... |
        And I should see the "Alphabetical navigation" list containing "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"
