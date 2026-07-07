import { API_URL } from "../config/api";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  email: string;
  celular: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

export async function login(
  data: LoginRequest
): Promise<AuthResponse> {

  const response = await fetch(
    `${API_URL}/auth/login`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    throw new Error("Email o contraseña incorrectos");
  }

  return response.json();
}

export async function register(
  data: RegisterRequest
): Promise<AuthResponse> {

  const response = await fetch(
    `${API_URL}/auth/register`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );

  const body = await response.json();

  if (!response.ok) {
    throw new Error(body.message ?? "Error al registrar usuario");
  }

  return body;
}