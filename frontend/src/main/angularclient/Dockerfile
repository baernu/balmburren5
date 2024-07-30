#FROM node:latest as build
#WORKDIR /app
#COPY ../../.. .
#RUN npm install
#RUN npm run build --prod

#FROM nginx:latest
#COPY --from=build /app/dist/frontend /usr/share/nginx/html
#COPY nginx.conf /etc/nginx/nginx.conf
#EXPOSE 4200


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

EXPOSE 4200
