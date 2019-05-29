FROM node:lts

WORKDIR /app

COPY package.json .
COPY package-lock.json .
RUN NODE_ENV=development npm install --no-audit --unsafe-perm
CMD ["npm", "run", "start"]