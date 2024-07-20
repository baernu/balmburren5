FROM node:latest as build
WORKDIR /app
COPY ../../.. .
RUN npm install
RUN npm run build --prod

FROM nginx:latest
COPY --from=build /app/dist/frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 4200
