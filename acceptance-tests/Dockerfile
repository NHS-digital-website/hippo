FROM mcr.microsoft.com/playwright
COPY src/test/node /e2e

WORKDIR /e2e
# Install dependencies
RUN npm install

# Install browsers
RUN npx playwright install
RUN npx playwright install-deps

# Run playwright test
#CMD npx cucumber-js
#CMD ["npx", "playwright", "test","--workers","1"]