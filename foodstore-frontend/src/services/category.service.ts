import { API_URL } from "../config/api";
import { getToken } from "../utils/localStorage";
import type { ICategoria } from "../types/category";

const getHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export async function getCategories(): Promise<ICategoria[]> {
  const response = await fetch(`${API_URL}/categorias`, {
    headers: getHeaders(),
  });

  if (!response.ok) {
    throw new Error("Error al obtener categorías");
  }

  return response.json();
}

export async function createCategory(data: {
  nombre: string;
  descripcion: string;
}): Promise<ICategoria> {

  const response = await fetch(`${API_URL}/categorias`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error("Error al crear categoría");
  }

  return response.json();
}

export async function deleteCategory(id: number): Promise<void> {
  const response = await fetch(`${API_URL}/categorias/${id}`, {
    method: "DELETE",
    headers: getHeaders(),
  });

  if (!response.ok) {
    const body = await response.json();
    throw new Error(body.message ?? "Error al eliminar categoría");
  }
}

export async function updateCategory(
  id: number,
  data: {
    nombre: string;
    descripcion: string;
  }
): Promise<ICategoria> {
  const response = await fetch(`${API_URL}/categorias/${id}`, {
    method: "PUT",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const body = await response.json();
    throw new Error(body.message ?? "Error al actualizar categoría");
  }

  return response.json();
}