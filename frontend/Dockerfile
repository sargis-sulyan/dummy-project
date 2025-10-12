# ====== Build stage ======
FROM node:22 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# production build
RUN npm run build

# ====== Runtime stage (Nginx) ======
FROM nginx:alpine
# Serve the Angular dist
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
# Nginx config (serves SPA and proxies /api to backend)
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
