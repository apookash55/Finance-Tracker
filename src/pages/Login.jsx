import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

function Login() {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [message, setMessage] = useState("");
  const navigate = useNavigate(); // for navigation after login

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("/auth/login", formData);
      // Save JWT to localStorage
      localStorage.setItem("token", response.data.token);
      // Optionally, save username for display
      localStorage.setItem("username", response.data.username);
      // Redirect to dashboard
      navigate("/dashboard");
    } catch (error) {
      setMessage(error.response?.data?.message || "Login failed.");
    }
  };

  return (
    <div style={{ maxWidth: "400px", margin: "2rem auto" }}>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          required
          style={{ display: "block", margin: "10px 0", padding: "8px", width: "100%" }}
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
          style={{ display: "block", margin: "10px 0", padding: "8px", width: "100%" }}
        />
        <button type="submit" style={{ padding: "10px 15px" }}>Login</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}

export default Login;
