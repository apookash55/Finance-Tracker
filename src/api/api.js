import axios from "axios";

// Use your backend VM IP here
const api = axios.create({
  baseURL: "http://localhost:8080/api/v1", // change to backend IP if needed
});

// Automatically attach token if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
