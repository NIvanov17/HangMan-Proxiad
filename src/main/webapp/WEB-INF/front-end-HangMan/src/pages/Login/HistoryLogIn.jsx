import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import "./Login.css";

const HistoryLogIn = () => {

    const [username, setUsername] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();

    const navigateToHistory = () => {
        navigate('/history', { state: { username } });
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then(() => {
                setIsPending(false);
                navigateToHistory();
            })
        }, 500)
    }

    return (
        <div className="login">
            <div className="login-form">
                <h2>History</h2>
                <img src={hangmanImage} alt="" />
                <form onSubmit={handleSubmit}>
                    <label>Enter username:</label>
                    <input
                        type="text"
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    ></input>
                    {!isPending && <button>Submit</button>}
                    {isPending && <button disabled>Submitting...</button>}
                </form>
            </div>
        </div>
    );
}

export default HistoryLogIn;