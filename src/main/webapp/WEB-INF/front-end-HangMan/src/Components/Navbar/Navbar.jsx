import { Link, Navigate, useNavigate } from "react-router-dom";
import "./Navbar.css";
import { useEffect, useState } from "react";

const Navbar = () => {

    const [token, setToken] = useState('');
    const [username, setUsername] = useState('');
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const navigateHome = () => {
        navigate('/');
    }

    useEffect(() => {
        const storedUsername = sessionStorage.getItem('username');
        const storedToken = sessionStorage.getItem('token');
        if (storedUsername && storedToken) {
            setToken(storedToken);
            setUsername(storedToken);
        }
    }, [username]);

    const handleLogOut = () => {
        const token = sessionStorage.getItem('token');
        console.log(token);
        fetch('http://localhost:8080/api/v1/players/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        }).then((res) => {
            if (res.ok) {
                sessionStorage.removeItem('token');
                sessionStorage.removeItem('username');
                setToken('');
                setUsername('');
                navigateHome();
            }
        }).catch(err => {
            setError(err);
        })
    };

    return (
        <nav className="nav-bar">
            <a href="/" className="nav-title">
                <h1>Hang Man</h1>
            </a>
            <div className="links">
                <Link to="/">Home</Link>
                <Link to="/statistics">Statistics</Link>
                <Link to="/rankings">Rankings</Link>
                <Link to="/history">History</Link>
                <Link to="/admin">Admin</Link>
                {!token && (
                    <>
                        <Link to="/register" className="btn">Register</Link>
                        <Link to="/login" className="btn">Log In</Link>
                    </>
                )}
                {token && (
                    <>
                        <button onClick={handleLogOut} className="btn">Log Out</button>
                    </>
                )}
            </div>
        </nav>
    );
}

export default Navbar;


