


FROM node:latest AS build

#WORKDIR /app
#
#COPY ../../.. .
#
#RUN npm install
#
#RUN npm install -g @angular/cli
#
#COPY . .
#
#RUN ng build --configuration=production
#
#FROM nginx:latest
#
#COPY --from=build app/dist/angularclient /usr/share/nginx/html
#EXPOSE 80


# Stage 0, "build-stage", based on Node.js, to build and compile the frontend
FROM node:latest AS  build-stage
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY ./ /app/
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration

# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM nginx:1.15
#Copy ci-dashboard-dist
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
#Copy default nginx configuration
COPY ./nginx-custom.conf /etc/nginx/conf.d/default.conf

EXPOSE 8081
