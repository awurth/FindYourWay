docker build -t sameersbn/postgresql github.com/sameersbn/docker-postgresql && docker run --name postgresql -itd --restart always --publish 5432:5432 --volume 
/srv/docker/postgresql:/var/lib/postgresql sameersbn/postgresql:9.6-2 && docker build --tag findyourway . && docker run -p 8080:8080 -p 9999:9999 -it --link 
postgresql:postgres --name findyourway findyourway
