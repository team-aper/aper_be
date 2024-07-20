FROM nginx:latest                           
LABEL name="demo"                           

COPY ./src/main/resources/templates/login.html /usr/share/nginx/html/index.html   

EXPOSE 80                                            

CMD ["nginx", "-g", "daemon off;"]
