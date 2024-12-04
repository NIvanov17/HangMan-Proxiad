import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import "./Login.css";
import Error from './../Errors/Error';
import { validateUsername } from "../../utils/ValidationUtils";

const HistoryLogIn = () => {

    const [username, setUsername] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const [validationErr, setValidationErr] = useState('');

    const navigateToHistory = () => {
        navigate('/history', { state: { username } });
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        const validationMessage = validateUsername(username);
        if (validationMessage) {
            setValidationErr(validationMessage);
            setIsPending(false);
            return;
        }
        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/' + username, {
                method: 'POST'
            }).then((res) => {
                setIsPending(false);
                if (!res.ok) {
                    return res.json().then((errorData) => {
                        throw { message: errorData.message, ...errorData };
                    });
                }
                navigateToHistory();
            }).catch(err => {
                setError(err)
            })
        }, 500)
    }

    if (error) {
        return <Error error={error} />;
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
                    {validationErr && <p className="validation-error">{validationErr}</p>}
                    {!validationErr && <p className="validation-error"></p>}
                    {!isPending && <button>Submit</button>}
                    {isPending && <button disabled>Submitting...</button>}
                </form>
            </div>
        </div>
    );
}

export default HistoryLogIn;