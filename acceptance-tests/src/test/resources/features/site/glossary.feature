Feature: Ensure Glossary page displays required fields.


    Scenario: Check individual content fields (Glossary)
        Given I navigate to the "Glossary test document" page
        Then I should see glossary page titled "Glossary AcceptanceTestDocument"
        And I should also see:
            | Glossary Summary      | Glossary Lorem ipsum dolor sit amet, consectetur ... |
            | Glossary List         | B\nBananas\nA banana is an edible fruit ... |
        And I should see the list with title "Alphabetical navigation" containing:
            | A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z |
