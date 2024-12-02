import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./WordToGuess.css";

const WordToGuess = () => {

    const [word, setWord] = useState('');
    const [category, setCategory] = useState('');
    const [categories, setCategories] = useState([]);
    const location = useLocation();
    const { giver, guesser } = location.state || {};
    const [game, setGame] = useState(null);
    const navigate = useNavigate();

    const dto = {
        multiPlayerGameInputDTO: {
            giverUsername: giver,
            guesserUsername: guesser,
            wordToGuess: word,
            category: category
        }
    }

    useEffect(() => {
        fetch('http://localhost:8080/api/v1/categories')
            .then(res => {
                return res.json();
            }).then((data) => {
                setCategories(data);
            })
    }, []);


    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8080/api/v1/games', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dto)
        })
            .then(res => {
                return res.json();
            }).then(data => {
                navigate('/multi-player/games', {
                    state: {
                        gameDTO: data
                    }
                });
            })
    }

    return (

        <div className="word-giving">
            <form onSubmit={handleSubmit}>
                <label>Word To Guess:</label>
                <input type="text"
                    required
                    value={word}
                    onChange={(e) => setWord(e.target.value)}>
                </input>
                <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}>
                    <option value="" disabled>Select Category</option>
                    {categories.map((c) => (
                        <option key={c.id} value={c.name}>{c.name}</option>
                    ))}
                </select>
                <button>Submit</button>
            </form>
        </div>
    );
}

export default WordToGuess;