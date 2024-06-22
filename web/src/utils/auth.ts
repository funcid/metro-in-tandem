import { writable, derived } from "svelte/store";

const parseJwt = (token: string) => {
    if (!token) return null;

    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
        window
            .atob(base64)
            .split("")
            .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
            .join("")
    );

    return JSON.parse(jsonPayload);
};

let initialJWT = localStorage.getItem("JWT") || null;
export let JWT = writable(initialJWT);
export let username = writable(null);

JWT.subscribe((value) => {
    if (value) {
        localStorage.setItem("JWT", value);
        const payload = parseJwt(value);
        if (payload) {
            username.set(payload.sub);
        } else {
            username.set(null);
        }
    } else {
        username.set(null);
        localStorage.removeItem("JWT");
    }
});

export let isAuthenticated = derived(JWT, ($JWT) => !!$JWT);

isAuthenticated.subscribe((auth) => {
    if (auth && window.location.hash === "#/login") {
        window.location.href = "/";
    }
});

export const login = async (username: string, password: string, API_HOST: string) => {
    try {
        const response = await fetch(`${API_HOST}auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });

        const data = await response.json();

        if (response.ok) {
            JWT.set(data.token);
            return { success: true, token: data.token };
        } else {
            return { success: false, message: data.message || "Unknown error" };
        }
    } catch (error) {
        return { success: false, message: "Проверьте почту и пароль" };
    }
};