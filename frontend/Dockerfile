FROM node

WORKDIR /app

COPY package.json /app

RUN npm install

RUN npm install @material-ui/core

RUN npm install @material-ui/icons

COPY . /app

EXPOSE 3000

CMD ["npm", "start"]
