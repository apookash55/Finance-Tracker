import { useEffect, useState } from "react";
import api from "../api/api";

function Dashboard() {
  const [userData, setUserData] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await api.get("/users/me"); // your backend endpoint
        setUserData(response.data);
      } catch (error) {
        setMessage(error.response?.data?.message || "Failed to fetch data.");
      }
    };

    fetchUserData();
  }, []);

  if (message) return <p>{message}</p>;
  if (!userData) return <p>Loading...</p>;

  return (
    <div style={{ maxWidth: "600px", margin: "2rem auto" }}>
      <h2>Dashboard</h2>
      <p>Welcome, {userData.username}!</p>
      <p>Email: {userData.email}</p>
      <p>Last Login: {userData.lastLogin}</p>

      {/* You can later add accounts, categories, transactions here */}
    </div>
  );
}

export default Dashboard;
