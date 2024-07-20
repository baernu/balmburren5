#FROM node:latest as build
#WORKDIR /app
#COPY ../../.. .
#RUN npm install
#RUN npm run build --prod

#FROM nginx:latest
#COPY --from=build /app/dist/frontend /usr/share/nginx/html
#COPY nginx.conf /etc/nginx/nginx.conf
#EXPOSE 4200


FROM node:latest AS build

WORKDIR /app

COPY ../../.. .

RUN npm install

RUN npm install -g @angular/cli

COPY . .

RUN ng build --configuration=production

FROM nginx:latest

COPY --from=build app/dist/angularclient /usr/share/nginx/html

EXPOSE 4200
